// Empty constructor
function PluginName() {}

// The function that passes work along to native shells
// Message is a string, duration may be 'long' or 'short'
PluginName.prototype.newActivity = function(message, duration, successCallback, errorCallback) {
    cordova.exec(function(res){}, function(err){}, "PluginName", "new_activity", []);
}

// Installation constructor that binds VdoCypherTest to window
PluginName.install = function() {
    if (!window.plugins) {
        window.plugins = {};
    }
    window.plugins.pluginName = new PluginName();
    return window.plugins.pluginName;
};
cordova.addConstructor(PluginName.install);