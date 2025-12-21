FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Fetch your replacement driver folder as an additional git "subfetch"
SRC_URI:append = " \
    git://github.com/raphaelscholle/hantro_v4l2.git;protocol=https;branch=testing_live_update;destsuffix=hantro_v4l2;name=hantro_v4l2 \
    file://hantro_v4l2/Kconfig \
    file://hantro_v4l2/Makefile \
"

# Pin that additional fetch to your latest commit
SRCREV_hantro_v4l2 = "fa7026fe3cd38304882cb1c666f661ee2470c0d1"

# Ensure Yocto's multi-SRCREV handling includes this extra fetch
SRCREV_FORMAT:append = "_hantro_v4l2"

do_patch:append() {
    bbnote "Replacing drivers/mxc/hantro_v4l2 with OpenHD custom version"
    rm -rf ${S}/drivers/mxc/hantro_v4l2
    mkdir -p ${S}/drivers/mxc
    cp -a ${WORKDIR}/hantro_v4l2 ${S}/drivers/mxc/hantro_v4l2
}
