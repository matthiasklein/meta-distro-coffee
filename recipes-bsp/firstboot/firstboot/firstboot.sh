#!/bin/sh

FIRSTBOOT_SCRIPTS_PATH="/#SYSCONFDIR#/firstboot.d"
OVERLAY_PATH="/media/rfs/ro"

if mount | grep overlay > /dev/null; then
    echo "system runs with overlayfs"
    SYSTEM_RUNS_WITH_OVERLAYFS=1
    ROOT_PREFIX="${OVERLAY_PATH}"
else
    ROOT_PREFIX=""
fi

if [ "$SYSTEM_RUNS_WITH_OVERLAYFS" = 1 ]; then
    echo "mounting rootfs rw"
	mount $OVERLAY_PATH -o remount,rw
fi

run-parts -a root_prefix="${ROOT_PREFIX}" $FIRSTBOOT_SCRIPTS_PATH

echo "removing firstboot service ..."
rm -r ${ROOT_PREFIX}${FIRSTBOOT_SCRIPTS_PATH}
rm ${ROOT_PREFIX}/lib/systemd/system/firstboot.service
rm ${ROOT_PREFIX}/#SBINDIR#/firstboot.sh
rm ${ROOT_PREFIX}/etc/systemd/system/multi-user.target.wants/firstboot.service
rm ${ROOT_PREFIX}/lib/systemd/system-preset/*-firstboot.preset

if [ "$SYSTEM_RUNS_WITH_OVERLAYFS" = 1 ]; then
    echo "mounting rootfs ro"
    # This "ro" mount will not work. See firstboot_1.0.bb for notes about the known issue ...
	mount $OVERLAY_PATH -o remount,ro > /dev/null 2>&1
fi

sync

