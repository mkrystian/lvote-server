(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('UserGroupDialogController', UserGroupDialogController);

    UserGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserGroup', 'Voting', 'User'];

    function UserGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserGroup, Voting, User) {
        var vm = this;

        vm.userGroup = entity;
        vm.clear = clear;
        vm.save = save;
        vm.votings = Voting.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userGroup.id !== null) {
                UserGroup.update(vm.userGroup, onSaveSuccess, onSaveError);
            } else {
                UserGroup.save(vm.userGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lvoteApp:userGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
