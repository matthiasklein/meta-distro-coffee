# Create custom settings script to become included by environment script

fakeroot do_create_distro_sdk_env_file() {
	mkdir -p ${D}${SDKPATHNATIVE}/environment-setup.d
	script=${D}${SDKPATHNATIVE}/environment-setup.d/distro-env.sh

	echo 'echo "Applying Yocto SDK environment settings..."' > $script
	echo 'cat ${OECORE_NATIVE_SYSROOT}/../../sdkinfo.txt' >> $script
	echo 'echo "TARGET_PREFIX=${TARGET_PREFIX}"' >> $script
	echo 'export PS1="[${DISTRO}-sdk|${MACHINE}] $PS1"' >> $script
}

# Deploy generic link for environment setup
do_install:append () {
	mkdir -p ${D}/${SDKPATH}/
	cd ${D}/${SDKPATH}/
	ln -sf environment-setup-${REAL_MULTIMACH_TARGET_SYS} environment-setup
	cd -
}

FILES:${PN} += "${SDKPATH}/environment-setup"

addtask create_distro_sdk_env_file after do_install before do_package

