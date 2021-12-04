DESCRIPTION = "Populate /sdi filesystem on first boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
	file://populate-sdi.sh \
	file://service \
	file://service.pub \
"
DEPENDS = "firstboot"
S = "${WORKDIR}"

ARCHIVE_DIR ?= "${THISDIR}/populate-sdi/sdi"

FILES:${PN} = "${sysconfdir}/firstboot.d /opt/*.tar.gz"

do_configure() {
}

do_compile() {
    install -d ${B}/sdi-filesystem
    install -d ${B}/sdi-filesystem/.ssh
   
    install -m 0600 ${S}/service ${B}/sdi-filesystem/.ssh
    install -m 0600 ${S}/service.pub ${B}/sdi-filesystem/.ssh
    install -m 0600 ${S}/service.pub ${B}/sdi-filesystem/.ssh/authorized_keys
   
    cd ${B}/sdi-filesystem
    tar --owner root --group root -czf ${B}/${PN}-${PV}.tar.gz .
    cd -
}

do_install() {
    install -d ${D}/opt
    install -m 0644 ${B}/${PN}-${PV}.tar.gz ${D}/opt

	install -d ${D}${sysconfdir}/firstboot.d/
	install -m 0755 ${WORKDIR}/populate-sdi.sh ${D}/${sysconfdir}/firstboot.d/10populate-sdi
}

