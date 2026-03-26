SUMMARY = "QOpenHD companion application"
DESCRIPTION = "Qt-based OpenHD ground-station companion application"
HOMEPAGE = "https://github.com/OpenHD/QOpenHD"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ccabeb20df52b9236fcc6ea3d7e6f55"

SRC_URI = "gitsm://github.com/OpenHD/QOpenHD.git;protocol=https;branch=2.7-evo"
SRCREV = "f67981240797b3cb278887ada07133e49dcf5dbb"

PV = "2.7-evo+git${SRCPV}"

S = "${WORKDIR}/git"
QMAKE_PROFILES = "${S}/QOpenHD.pro"
EXTRA_QMAKEVARS_PRE += "CONFIG-=EnableSpeech"

inherit qmake5_base pkgconfig

DEPENDS += " \
    ffmpeg \
    gstreamer1.0 \
    gstreamer1.0-plugins-base \
    libdrm \
    qtbase \
    qtcharts \
    qtdeclarative \
    qtlocation \
    qttools-native \
"

RDEPENDS:${PN} += " \
    ffmpeg \
    fontconfig \
    gstreamer1.0 \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-ugly \
    gstreamer1.0-libav \
    libdrm \
    openhd-sys-utils \
    qtbase \
    qtcharts \
    qtdeclarative \
    qtlocation \
"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}/usr/local/bin
    install -d ${D}/usr/local/share/qopenhd

    qopenhd_bin=""
    for candidate in \
        ${B}/release/QOpenHD \
        ${B}/QOpenHD \
        ${S}/release/QOpenHD \
        ${S}/QOpenHD \
    ; do
        if [ -x "$candidate" ]; then
            qopenhd_bin="$candidate"
            break
        fi
    done

    if [ -z "$qopenhd_bin" ]; then
        bbfatal "QOpenHD binary not found in ${B} or ${S}; check do_compile output"
    fi

    install -m 0755 "$qopenhd_bin" ${D}${bindir}/QOpenHD
    ln -sf ${bindir}/QOpenHD ${D}/usr/local/bin/QOpenHD

    install -m 0644 ${S}/rock_qt_eglfs_kms_config.json ${D}/usr/local/share/qopenhd/
    install -m 0644 ${S}/rpi_qt_eglfs_kms_config.json ${D}/usr/local/share/qopenhd/
}

do_compile() {
    if [ ! -e Makefile -a ! -e makefile -a ! -e GNUmakefile ]; then
        bbnote "Makefile missing, re-running qmake for QOpenHD.pro"
        ${OE_QMAKE_QMAKE} ${EXTRA_QMAKEVARS_PRE} ${QMAKE_PROFILES} ${EXTRA_QMAKEVARS_POST}
    fi

    oe_runmake
}

FILES:${PN} += " \
    ${bindir}/QOpenHD \
    /usr/local/bin/QOpenHD \
    /usr/local/share/qopenhd \
    /usr/local/share/qopenhd/rock_qt_eglfs_kms_config.json \
    /usr/local/share/qopenhd/rpi_qt_eglfs_kms_config.json \
"
