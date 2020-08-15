

module.exports = function (ctx) {
  // If Android platform is not installed, don't even execute
  if (ctx.opts.cordova.platforms.indexOf('android') < 0)
    return;

  var fs = require('fs'),
    path = require('path');

  var deferral ;

  var platformSourcesRoot = path.join(ctx.opts.projectRoot, 'platforms/android/app/src/main');
  var pluginSourcesRoot = path.join(ctx.opts.plugin.dir, 'src/android');

  var androidPluginsData = JSON.parse(fs.readFileSync(path.join(ctx.opts.projectRoot, 'plugins', 'android.json'), 'utf8'));
  var appPackage = androidPluginsData.installed_plugins[ctx.opts.plugin.id]['PACKAGE_NAME'];

  console.log('appPackage', appPackage);

  return deferral;
}