(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('EncryptionDataDetailController', EncryptionDataDetailController);

    EncryptionDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'EncryptionData'];

    function EncryptionDataDetailController($scope, $rootScope, $stateParams, previousState, entity, EncryptionData) {
        var vm = this;

        vm.encryptionData = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('lvoteApp:encryptionDataUpdate', function(event, result) {
            vm.encryptionData = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
