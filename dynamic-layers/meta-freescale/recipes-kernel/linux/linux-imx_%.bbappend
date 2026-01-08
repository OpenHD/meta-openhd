FILESEXTRAPATHS:prepend := "${THISDIR}/linux-imx:"

SRC_URI:append = " file://os08a20-wlan.config"
DELTA_KERNEL_DEFCONFIG:append = " os08a20-wlan.config"
