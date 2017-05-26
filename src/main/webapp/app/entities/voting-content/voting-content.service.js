(function() {
    'use strict';
    angular
        .module('lvoteApp')
        .factory('VotingContent', VotingContent);

    VotingContent.$inject = ['$resource'];

    function VotingContent ($resource) {
        var resourceUrl =  'api/voting-contents/:id';

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
