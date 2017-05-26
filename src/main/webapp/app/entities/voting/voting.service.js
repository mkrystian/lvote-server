(function() {
    'use strict';
    angular
        .module('lvoteApp')
        .factory('Voting', Voting);

    Voting.$inject = ['$resource', 'DateUtils'];

    function Voting ($resource, DateUtils) {
        var resourceUrl =  'api/votings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDateTime = DateUtils.convertLocalDateFromServer(data.startDateTime);
                        data.endDateTime = DateUtils.convertLocalDateFromServer(data.endDateTime);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.startDateTime = DateUtils.convertLocalDateToServer(copy.startDateTime);
                    copy.endDateTime = DateUtils.convertLocalDateToServer(copy.endDateTime);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.startDateTime = DateUtils.convertLocalDateToServer(copy.startDateTime);
                    copy.endDateTime = DateUtils.convertLocalDateToServer(copy.endDateTime);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
