SUMMARY = "OpenHD Web user interface"
LICENSE = "Unlicense"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d88e9e08385d2a17052dac348bde4bc1"

SRC_URI = "git://github.com/OpenHD/OpenHD-WebUI.git;protocol=https;branch=development"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

inherit systemd

# Allow internet during do_compile for dotnet and npm
do_compile[network] = "1"

# Runtime Identifier map
python __anonymous() {
    arch = d.getVar("TARGET_ARCH")
    rid = {
        "aarch64": "linux-arm64",
        "armv7a": "linux-arm",
        "x86_64": "linux-x64"
    }.get(arch)

    if not rid:
        bb.fatal("Unsupported arch '%s' for DOTNET_TARGET" % arch)

    d.setVar("DOTNET_TARGET", rid)
}

# Use host-installed dotnet
DOTNET_NATIVE ?= "${@bb.utils.which(d.getVar('PATH'), 'dotnet')}"

do_compile() {
    if [ -z "${DOTNET_NATIVE}" ]; then
        bbfatal "Host 'dotnet' not found. Please install dotnet-sdk-9.0 via apt."
    fi

    ${DOTNET_NATIVE} publish \
        -c Release \
        -r ${DOTNET_TARGET} \
        --self-contained \
        -o ${B}/publish \
        src/OpenHdWebUi.Server/OpenHdWebUi.Server.csproj
}

do_install() {
    install -d ${D}/usr/local/share/openhd/web-ui
    cp -r ${B}/publish/* ${D}/usr/local/share/openhd/web-ui/

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/openhd-web-ui.service ${D}${systemd_unitdir}/system/
}

# Runtime dependencies (fix for file-rdeps QA error)
RDEPENDS:${PN} += "zlib"
INSANE_SKIP:${PN} += "libdir buildpaths file-rdeps"

# Suppress QA warnings about library paths and build paths
INSANE_SKIP:${PN} += "libdir buildpaths"

# Systemd integration
SYSTEMD_SERVICE:${PN} = "openhd-web-ui.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

# File list
FILES:${PN} += " \
    /usr/local/share/openhd/web-ui/ \
    ${systemd_unitdir}/system/openhd-web-ui.service \
"
