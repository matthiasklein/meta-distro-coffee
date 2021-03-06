# basic image features
SUMMARY = "A console-only image that fully supports the target device hardware."

IMAGE_FEATURES += "package-management hwcodecs ssh-server-openssh "

inherit core-image
inherit populate_sdk_qt6_base

TOOLCHAIN_HOST_TASK:append = " nativesdk-populate-sdk"
IMAGE_INSTALL:append = " readonly-rootfs-overlay firstboot populate-sdi shell-profile"

# full "ps aux"
IMAGE_INSTALL:append = " procps bash tar htop picocom curl"

# device tree compiler to dump running DT from /sys/firmware/devicetree/base
IMAGE_INSTALL:append = " dtc"

# Dependencies for PyCharm Remote helpers ("Run" & "Debug")
IMAGE_INSTALL:append = " python3-multiprocessing python3-compression python3-xmlrpc"

# NetworkManager
IMAGE_INSTALL:append = " networkmanager networkmanager-nmcli"
#IMAGE_INSTALL:append = " modemmanager"

IMAGE_INSTALL:append = " i2c-tools"
IMAGE_INSTALL:append = " gdbserver gcc-sanitizers boost paho-mqtt-c paho-mqtt-cpp"

TOOLCHAIN_HOST_TASK:append = " nativesdk-qtbase-dev nativesdk-qtbase-tools nativesdk-qttools-dev nativesdk-qttools-tools"
IMAGE_INSTALL:append = " qtbase qtserialport qtserialbus"

TOOLCHAIN_HOST_TASK:append = " nativesdk-protobuf-compiler nativesdk-protobuf-c nativesdk-grpc nativesdk-grpc-dev nativesdk-grpc-compiler"
IMAGE_INSTALL:append = " protobuf grpc"

ROOT_USER_PASSWORD ?= "toor"
ROOTFS_POSTPROCESS_COMMAND:append = " set_root_passwd;"
set_root_passwd() {
   ROOTPW_ENCRYPTED="$(openssl passwd -6 -salt xyz ${ROOT_USER_PASSWORD})"
   sed -i "s%^root:[^:]*:%root:${ROOTPW_ENCRYPTED}:%" ${IMAGE_ROOTFS}/etc/shadow
}

ROOTFS_POSTPROCESS_COMMAND:append = " distro_imageinfo_function; "
distro_imageinfo_function() {
    set -- "${BSPDIR}/yocto"/meta-yocto-*
    [ -e "$1" ] || { echo "WARNING: no matching directory found which start with meta-yocto-*!" >&2; }
    [ "$#" -gt 1 ] && echo "WARNING: $# matching directories found which start with meta-yocto-*; only taking first" >&2
    YOCTO_LAYER_DIR=$1

    if [ -d "$YOCTO_LAYER_DIR" ]; then
        YOCTO_LAYER_NAME=$(basename ${YOCTO_LAYER_DIR})
        IMAGE_VERSION="${YOCTO_LAYER_NAME}"

        # .localversion is used to add e.g. "-unstable-" into the version string
        if [ -f "${BSPDIR}/.localversion" ]; then
             LOCALVERSION=$(cat ${BSPDIR}/.localversion)
             IMAGE_VERSION="${IMAGE_VERSION}-${LOCALVERSION}"
        fi

        cd ${YOCTO_LAYER_DIR}
        if [ -d .git ]; then
            YOCTO_LAYER_VERSION=$(git describe --always)
        else
            YOCTO_LAYER_VERSION="#####"
        fi
        IMAGE_VERSION="${IMAGE_VERSION}-${YOCTO_LAYER_VERSION}"
        cd -
    fi

	echo -n "BUILD_DATE=" > ${IMAGE_ROOTFS}/etc/imageinfo.txt
	date +%FT%T%z >> ${IMAGE_ROOTFS}/etc/imageinfo.txt
	echo "IMAGE="${BPN} >> ${IMAGE_ROOTFS}/etc/imageinfo.txt
	echo "MACHINE="${MACHINE} >> ${IMAGE_ROOTFS}/etc/imageinfo.txt
    echo "VERSION=${IMAGE_VERSION}" >> ${IMAGE_ROOTFS}/etc/imageinfo.txt
}

POPULATE_SDK_POST_HOST_COMMAND:append:task-populate-sdk = " distro_sdkinfo_function; "
distro_sdkinfo_function() {
    set -- "${BSPDIR}/yocto"/meta-yocto-*
    [ -e "$1" ] || { echo "WARNING: no matching directory found which start with meta-yocto-*!" >&2; }
    [ "$#" -gt 1 ] && echo "WARNING: $# matching directories found which start with meta-yocto-*; only taking first" >&2
    YOCTO_LAYER_DIR=$1

    if [ -d "$YOCTO_LAYER_DIR" ]; then
        YOCTO_LAYER_NAME=$(basename ${YOCTO_LAYER_DIR})
        IMAGE_VERSION="${YOCTO_LAYER_NAME}"

        # .localversion is used to add e.g. "-unstable-" into the version string
        if [ -f "${BSPDIR}/.localversion" ]; then
             LOCALVERSION=$(cat ${BSPDIR}/.localversion)
             IMAGE_VERSION="${IMAGE_VERSION}-${LOCALVERSION}"
        fi

        cd ${YOCTO_LAYER_DIR}
        if [ -d .git ]; then
            YOCTO_LAYER_VERSION=$(git describe --always)
        else
            YOCTO_LAYER_VERSION="#####"
        fi
        IMAGE_VERSION="${IMAGE_VERSION}-${YOCTO_LAYER_VERSION}"
        cd -
    fi

	mkdir -p ${SDK_OUTPUT}/${SDKPATH}/
	echo -n "BUILD_DATE=" > ${SDK_OUTPUT}/${SDKPATH}/sdkinfo.txt
	date +%FT%T%z >> ${SDK_OUTPUT}/${SDKPATH}/sdkinfo.txt
	echo "SDK_NAME="${SDK_NAME} >> ${SDK_OUTPUT}/${SDKPATH}/sdkinfo.txt
	echo "IMAGE="${SDK_TARGETS} >> ${SDK_OUTPUT}/${SDKPATH}/sdkinfo.txt
	echo "MACHINE="${MACHINE} >> ${SDK_OUTPUT}/${SDKPATH}/sdkinfo.txt
    echo "VERSION=${IMAGE_VERSION}" >> ${SDK_OUTPUT}/${SDKPATH}/sdkinfo.txt
}

