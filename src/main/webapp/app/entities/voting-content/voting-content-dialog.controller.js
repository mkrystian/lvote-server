(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingContentDialogController', VotingContentDialogController);

    VotingContentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VotingContent'];

    function VotingContentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VotingContent) {
        var vm = this;

        vm.votingContent = entity;
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
            if (vm.votingContent.id !== null) {
                VotingContent.update(vm.votingContent, onSaveSuccess, onSaveError);
            } else {
                VotingContent.save(vm.votingContent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('lvoteApp:votingContentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
