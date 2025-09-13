SUMMARY = "OpenHD: Open-source digital video transmission system"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"



SRC_URI = "gitsm://github.com/openhd/OpenHD.git;protocol=https;branch=dev-release"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/OpenHD"

inherit cmake pkgconfig systemd

DEPENDS = "flac poco libsodium gstreamer1.0 gstreamer1.0-plugins-base libpcap libusb1 libv4l"

RDEPENDS:${PN} += " \
  gstreamer1.0 \
  gstreamer1.0-plugins-base \
  gstreamer1.0-plugins-good \
  gstreamer1.0-plugins-bad \
  gstreamer1.0-plugins-ugly \
  gstreamer1.0-libav \
  v4l-utils \
"

# Install and enable systemd service so OpenHD starts on boot
SYSTEMD_SERVICE = "openhd.service"
SYSTEMD_AUTO_ENABLE = "enable"

do_install:append() {
    # Install systemd service
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/systemd/system/openhd.service ${D}${systemd_unitdir}/system

    # Create required directories and files
    install -d ${D}/Video
    install -d ${D}/boot/openhd
    touch ${D}/boot/openhd/ground.txt
}

FILES:${PN} += "${bindir}/* ${systemd_unitdir}/system/openhd.service /Video /boot/openhd/ground.txt"
