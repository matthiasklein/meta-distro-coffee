SUMMARY = "Runs scripts on first boot of the target device"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# Known issue in conjunction with overlayfs:
# ==========================================
# After "rw" mounting /media/rfs/ro, a subsequent "ro" mount (in firstboot.sh) is not successful.
# But this does not seem to have a negative impact on the overlayfs operation:
# files edited from the "Upper" area (e.g. /etc/hosts) are not automatically modified in the "Lower" area (/media/rfs/ro/etc/hosts).
# Also, newly created files in the "Upper" area do not appear in the "Lower" area.
#
# That means only files which are really explicitly edited in the "Lower" area are kept. The bug has no effect.
# It is strange that a manual "rw" and subsequent "ro" mount (e.g. in the second boot process) is possible.

SRC_URI = " \
	file://firstboot.service \
	file://firstboot.sh \
	file://banner.sh \
"

S = "${WORKDIR}"

inherit allarch systemd
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE:${PN} = "firstboot.service"

FILES:${PN} = "${sbindir} ${sysconfdir}/firstboot.d ${systemd_unitdir}/system/firstboot.service"

do_configure() {
}

do_compile () {
}

do_install() {
    # systemd unit
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/firstboot.service ${D}${systemd_unitdir}/system
  
    # executor script
	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/firstboot.sh ${D}${sbindir}

    # example task
	install -d ${D}${sysconfdir}/firstboot.d/
	install -m 0755 ${WORKDIR}/banner.sh ${D}/${sysconfdir}/firstboot.d/01banner

    # replace the placeholders with the corresponding directories
	sed -i -e 's:#SYSCONFDIR#:${sysconfdir}:g' \
               -e 's:#SBINDIR#:${sbindir}:g' \
               -e 's:#BASE_BINDIR#:${base_bindir}:g' \
               -e 's:#LOCALSTATEDIR#:${localstatedir}:g' \
               ${D}${systemd_unitdir}/system/firstboot.service ${D}${sbindir}/firstboot.sh ${D}${sysconfdir}/firstboot.d/*
}

