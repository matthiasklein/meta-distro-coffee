FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://disable-gzip.cfg \
            file://enable-date-nano.cfg \
            file://enable-nice.cfg \
            file://enable-xargs-features.cfg \
            file://enable-reboot-wait-for-init.cfg \
            file://enable-bc.cfg \
            file://disable-net-tools.cfg \
            file://disable-vi.cfg \
            file://disable-fdisk.cfg \
            file://disable-wget.cfg \
            "

RDEPENDS:${PN} += "gzip iproute2 net-tools util-linux nano vim wget"
