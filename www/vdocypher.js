// Empty constructor
function VdoCypherTest() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
VdoCypherTest.prototype.show = function(message, duration, successCallback, errorCallback) {
  var options = {};
  options.message = message;
  options.duration = duration;
  cordova.exec(successCallback, errorCallback, 'VdoCypherTest', 'show', [options]);
}

// Installation constructor that binds VdoCypherTest to window
VdoCypherTest.install = function() {
  if (!window.plugins) {
    window.plugins = {};
  }
  window.plugins.vdocypher = new VdoCypherTest();
  return window.plugins.vdocypher;
};
cordova.addConstructor(VdoCypherTest.install);