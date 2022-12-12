// .versionrc.js
const xmlUpdater = require('standard-version-updater-pom');
const xmlTracker = {
    filename: 'pom.xml',
    updater: xmlUpdater,
};
module.exports = {
    packageFiles: [xmlTracker],
    bumpFiles: [xmlTracker],
};
