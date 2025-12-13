SUMMARY = "OpenHD Web user interface"
LICENSE = "Unlicense"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=d88e9e08385d2a17052dac348bde4bc1"

SRC_URI = "git://github.com/OpenHD/OpenHD-WebUI.git;protocol=https;branch=development"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/git"

inherit systemd

# Allow internet access during do_compile (dotnet restore / npm)
do_compile[network] = "1"

# Map Yocto arch → .NET Runtime Identifier
python __anonymous() {
    arch = d.getVar("TARGET_ARCH")
    rid_map = {
        "aarch64": "linux-arm64",
        "armv7a":  "linux-arm",
        "x86_64":  "linux-x64",
    }

    rid = rid_map.get(arch)
    if not rid:
        bb.fatal("Unsupported TARGET_ARCH '%s' for OpenHD WebUI (.NET)" % arch)

    d.setVar("DOTNET_TARGET", rid)
}

do_compile() {
    echo "==== OpenHD WebUI build ===="
    echo "PATH=$PATH"

    DOTNET_NATIVE="$(command -v dotnet || true)"
    echo "DOTNET_NATIVE=$DOTNET_NATIVE"

    if [ -z "$DOTNET_NATIVE" ]; then
        bbfatal "dotnet not found in Yocto do_compile PATH"
    fi

    "$DOTNET_NATIVE" publish \
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
    install -m 0644 ${S}/openhd-web-ui.service \
        ${D}${systemd_unitdir}/system/openhd-web-ui.service
}

# Runtime deps (minimal, avoid QA noise)
RDEPENDS:${PN} += "zlib"

# Skip known-safe QA checks
INSANE_SKIP:${PN} += "libdir buildpaths file-rdeps"

# systemd integration
SYSTEMD_SERVICE:${PN} = "openhd-web-ui.service"
SYSTEMD_AUTO_ENABLE:${PN} = "enable"

FILES:${PN} += " \
    /usr/local/share/openhd/web-ui/ \
    ${systemd_unitdir}/system/openhd-web-ui.service \
"
