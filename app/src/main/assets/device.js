
(function($) {
    $(function() {
        Device = $.extend(Device, {}, {
            Data: {
                update: function(config) {
                    console.log(config);
                    var value;
                    if (config) {
                        value = JSON.stringify(config);
                    }
                    console.log(config);
                    console.log(value);
                    window.android.dataUpdate(value);
                }
            },
            Message: {
                show: function(config) {
                    var value;
                    if (config) {
                        value = JSON.stringify(config);
                    }
                    console.log(config);
                    console.log(value);
                    window.android.message(value);
                }
            },
            Progress: {
                show: function() {
                    console.log('show');
                    window.android.progressShow();
                },
                hide: function() {
                    console.log('hide');
                    window.android.progressHide();
                }
            },
            Image: {
                open: function(src) {

                    window.android.imageOpen(src);
                },
                upload: function(config) {
                    var value;
                    if (config) {
                        value = JSON.stringify(config);
                    }
                    console.log(config);
                    console.log(value);
                    window.android.imageUpload(value);
                }
            },
            initialize: function(config) {
                var value;
                if (config) {
                    value = JSON.stringify(config);
                }
                console.log(config);
                console.log(value);
                window.android.initialize(value);
            }
        });
        if (Device.onReady) {
            Device.onReady();
        }
    });
})(jQuery);