DESCRIPTION = "Special purpose SDK components"
LICENSE = "MIT"

EXCLUDE_FROM_WORLD = "1"

inherit base
BBCLASSEXTEND += "native nativesdk"


SRC_URI = " \
    file://bin/dsh \
    file://bin/dcp \
    file://bin/dtime \
    file://bin/ipscan \
    file://environment-setup.d/target-credentials.sh \
    file://ssh/service.pub \
    file://ssh/service \
"

do_install () {
    install -d ${D}/${bindir}
	install -m755 ${WORKDIR}/bin/dsh ${D}/${bindir}
	install -m755 ${WORKDIR}/bin/dcp ${D}/${bindir}
	install -m755 ${WORKDIR}/bin/dtime ${D}/${bindir}
	install -m755 ${WORKDIR}/bin/ipscan ${D}/${bindir}
	install -d ${D}${SDKPATHNATIVE}//environment-setup.d
	install -m755 ${WORKDIR}/environment-setup.d/target-credentials.sh ${D}${SDKPATHNATIVE}/environment-setup.d/
	install -d -m700 ${D}${SDKPATHNATIVE}/ssh/
	install -m600 ${WORKDIR}/ssh/service ${D}${SDKPATHNATIVE}/ssh/
	install -m600 ${WORKDIR}/ssh/service.pub ${D}${SDKPATHNATIVE}/ssh/
}

FILES:${PN} += "${SDKPATHNATIVE}/ssh/* ${SDKPATHNATIVE}/environment-setup.d/*"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

