SUMMARY = "Read only rootfs with overlay init script"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
DEPENDS = "virtual/kernel"
SRC_URI = "file://init-readonly-rootfs-overlay.sh \
    file://reboot-ro \
    file://reboot-rw \
    "

S = "${WORKDIR}"

do_install() {
        install -d "${D}/sbin"
        install -m 0755 ${WORKDIR}/init-readonly-rootfs-overlay.sh ${D}/sbin/init-overlay
        install -m 0755 ${WORKDIR}/reboot-ro ${D}/sbin/reboot-ro
        install -m 0755 ${WORKDIR}/reboot-rw ${D}/sbin/reboot-rw
        install -d "${D}/media/rfs/ro"
        install -d "${D}/media/rfs/rw"
}

FILES:${PN} += " /sbin/init-overlay /sbin/reboot-ro /sbin/reboot-rw /media/rfs"

# Due to kernel dependency
PACKAGE_ARCH = "${MACHINE_ARCH}"

