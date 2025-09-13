SUMMARY = "Realtek RTL88x2BU WiFi driver"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7e967c391aabf5b2e4cb3b193c0b5207"

inherit module

SRC_URI = "gitsm://github.com/OpenHD/rtl88x2bu.git;branch=master;protocol=https"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

DEPENDS += "virtual/kernel"

# Load driver automatically at boot
KERNEL_MODULE_AUTOLOAD += "88x2bu"

EXTRA_OEMAKE += "ARCH=${TARGET_ARCH} CROSS_COMPILE=${TARGET_PREFIX} KSRC=${STAGING_KERNEL_BUILDDIR}"
