SUMMARY = "OpenHD Web user interface"
LICENSE = "Unlicense"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d88e9e08385d2a17052dac348bde4bc1"

SRC_URI = "git://github.com/OpenHD/OpenHD-WebUI.git;protocol=https;branch=master"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/git"

inherit systemd

# Build the .NET web server
# This assumes the dotnet SDK is available in the build environment
# and will publish a self-contained application into ${B}/publish

DOTNET_TARGET ?= "linux-${TARGET_ARCH}"

do_compile() {
    dotnet publish -c Release -r ${DOTNET_TARGET} -o ${B}/publish src/OpenHdWebUi.Server/OpenHdWebUi.Server.csproj
}

do_install() {
    install -d ${D}/usr/local/share/openhd/web-ui
    cp -r ${B}/publish/* ${D}/usr/local/share/openhd/web-ui/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/openhd-web-ui.service ${D}${systemd_unitdir}/system/
}

SYSTEMD_SERVICE:${PN} = "openhd-web-ui.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

FILES:${PN} += " \
    /usr/local/share/openhd/web-ui/ \
    ${systemd_unitdir}/system/openhd-web-ui.service \
"
