(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('EncryptionDataController', EncryptionDataController);

    EncryptionDataController.$inject = ['EncryptionData'];

    function EncryptionDataController(EncryptionData) {

        var vm = this;

        vm.encryptionData = [];

        loadAll();

        function loadAll() {
            EncryptionData.query(function(result) {
                vm.encryptionData = result;
                vm.searchQuery = null;
            });
        }
    }
})();
