# basic image features
SUMMARY = "A console-only image that fully supports the target device hardware."

IMAGE_FEATURES += "package-management hwcodecs ssh-server-openssh "

inherit core-image

IMAGE_INSTALL:append = " readonly-rootfs-overlay firstboot populate-sdi shell-profile"

# full "ps aux"
IMAGE_INSTALL:append = " procps bash tar htop"

# NetworkManager
IMAGE_INSTALL:append = " networkmanager networkmanager-nmcli"
#IMAGE_INSTALL:append = " modemmanager"

ROOT_USER_PASSWORD ?= "toor"
ROOTFS_POSTPROCESS_COMMAND:append = " set_root_passwd;"
set_root_passwd() {
   ROOTPW_ENCRYPTED="$(openssl passwd -6 -salt xyz ${ROOT_USER_PASSWORD})"
   sed -i "s%^root:[^:]*:%root:${ROOTPW_ENCRYPTED}:%" ${IMAGE_ROOTFS}/etc/shadow
}

