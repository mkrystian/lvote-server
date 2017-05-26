(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('EncryptionDataDeleteController',EncryptionDataDeleteController);

    EncryptionDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'EncryptionData'];

    function EncryptionDataDeleteController($uibModalInstance, entity, EncryptionData) {
        var vm = this;

        vm.encryptionData = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EncryptionData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
