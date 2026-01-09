# poco upstream changed from 'master' to 'main':
# - kirkstone uses 1.11.2, with the pinned SRCREV in 'main'
# - scarthgap uses 1.12.5, with the pinned SRCREV *NOT* in 'main', but in a leftover branch
# FIXME: Both are fixed in upstream meta-openembedded, remove this bbappend after updating

POCO_FETCH_BRANCH = "${@bb.utils.contains('LAYERSERIES_CORENAMES', 'scarthgap', 'poco-1.12.5', 'main', d)}"

SRC_URI:remove = "git://github.com/pocoproject/poco.git;branch=master;protocol=https"
SRC_URI:append = " git://github.com/pocoproject/poco.git;branch=${POCO_FETCH_BRANCH};protocol=https"
