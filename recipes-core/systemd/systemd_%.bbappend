FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGECONFIG:remove = "networkd resolved nss-resolve timesyncd"

RDEPENDS:${PN} += "systemd-analyze"

SRC_URI += "file://noclear.conf"

FILES:${PN} += "${sysconfdir}/systemd/system/getty@.service.d"

do_install:append() {
    # Disable renaming of eth0 to e.g. enp0s3
    echo "# Disable Predictable Network Interface Names" > ${D}${rootlibexecdir}/systemd/network/99-default.link

    # Do not clear consoles
    install -d ${D}${sysconfdir}/systemd/system/getty@.service.d
    install -m0644 ${WORKDIR}/noclear.conf ${D}${sysconfdir}/systemd/system/getty@.service.d/
}

