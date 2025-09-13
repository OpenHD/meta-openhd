SUMMARY = "OpenHD: Open-source digital video transmission system"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"



SRC_URI = "gitsm://github.com/openhd/OpenHD.git;protocol=https;branch=dev-release"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git/OpenHD"

inherit cmake pkgconfig

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
FILES_${PN} += "${bindir}/*"

# Install staging directory files if needed (usually automatically handled by Yocto)
