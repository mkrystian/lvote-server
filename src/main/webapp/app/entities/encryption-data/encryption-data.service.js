(function() {
    'use strict';
    angular
        .module('lvoteApp')
        .factory('EncryptionData', EncryptionData);

    EncryptionData.$inject = ['$resource'];

    function EncryptionData ($resource) {
        var resourceUrl =  'api/encryption-data/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
