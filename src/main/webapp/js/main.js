var nrg = {
	ajaxOptions : {},
	defaultValidation : function() {
		// TODO
		console.log("call is using default data validation, always valid");
		return true;
	},

	ajaxCall : function(opts, callback) {
		var defaults = {
			validate : this.defaultValidation,
			makeDataAsArray : function(serverData) {
				return $.map(serverData, function(value, index) {
					return [ value ];
				});

			},
			success : function(serverData) {
				if (defaults.validate(serverData)) {
					callback(serverData);
					// callback(this.makeDataAsArray(serverData));
				}
			}
		};
		ajaxOptions = $.extend(defaults, opts);
		// TODO
		var textOpt = "";
		$.each(ajaxOptions, function(key, value) {
			if (!$.isFunction(value)) {
				textOpt += "\nkey:" + key + "\nvalue:" + value;
			}
		})
		console.debug("ajax call options:" + textOpt);

		return $.ajax(ajaxOptions);
	}
};
