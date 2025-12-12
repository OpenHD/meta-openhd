FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "git://github.com/raphaelscholle/hantro_v4l2.git;protocol=https;branch=main;destsuffix=hantro_v4l2"

do_patch:append() {
    bbnote "Replacing drivers/mxc/hantro_v4l2 with OpenHD custom version"
    rm -rf ${S}/drivers/mxc/hantro_v4l2
    cp -r ${WORKDIR}/hantro_v4l2 ${S}/drivers/mxc/hantro_v4l2
}
