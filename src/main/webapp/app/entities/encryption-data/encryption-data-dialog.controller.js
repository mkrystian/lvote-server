(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('EncryptionDataDialogController', EncryptionDataDialogController);

    EncryptionDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EncryptionData'];

    function EncryptionDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EncryptionData) {
        var vm = this;

        vm.encryptionData = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.encryptionData.id !== null) {
                EncryptionData.update(vm.encryptionData, onSaveSuccess, onSaveError);
            } else {
                EncryptionData.save(vm.encryptionData, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lvoteApp:encryptionDataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
