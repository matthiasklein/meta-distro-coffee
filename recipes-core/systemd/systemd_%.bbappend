PACKAGECONFIG:remove = "networkd resolved nss-resolve timesyncd"

RDEPENDS:${PN} += "systemd-analyze"

# Disable renaming of eth0 to e.g. enp0s3
do_install:append() {
    echo "# Disable Predictable Network Interface Names" > ${D}${rootlibexecdir}/systemd/network/99-default.link
}

