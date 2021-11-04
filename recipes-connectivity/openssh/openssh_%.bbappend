FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# otherwise /usr/libexec/sftp-server is missing
RDEPENDS:${PN} += "openssh-sftp-server"

