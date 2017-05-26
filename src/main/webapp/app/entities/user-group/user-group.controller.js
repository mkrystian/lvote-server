(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('UserGroupController', UserGroupController);

    UserGroupController.$inject = ['UserGroup'];

    function UserGroupController(UserGroup) {

        var vm = this;

        vm.userGroups = [];

        loadAll();

        function loadAll() {
            UserGroup.query(function(result) {
                vm.userGroups = result;
                vm.searchQuery = null;
            });
        }
    }
})();
