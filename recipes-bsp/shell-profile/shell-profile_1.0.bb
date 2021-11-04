SUMMARY = "Shell profile"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
	file://alias.sh \
"

S = "${WORKDIR}"

FILES:${PN} = "${sysconfdir}/profile.d"

do_configure() {
}

do_compile () {
}

do_install() {
	install -d ${D}${sysconfdir}/profile.d
	install -m 0755 ${WORKDIR}/alias.sh ${D}${sysconfdir}/profile.d
}

