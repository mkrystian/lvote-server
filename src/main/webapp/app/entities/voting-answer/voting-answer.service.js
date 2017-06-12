(function () {
    'use strict';
    angular
        .module('lvoteApp')
        .factory('VotingAnswer', VotingAnswer);

    VotingAnswer.$inject = ['$resource'];

    function VotingAnswer($resource) {
        var resourceUrl = 'api/voting-answers/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'}
        });
    }
})();
