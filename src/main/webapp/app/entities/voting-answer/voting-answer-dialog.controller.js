(function () {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingAnswerDialogController', VotingAnswerDialogController);

    VotingAnswerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VotingAnswer', 'VotingContent'];

    function VotingAnswerDialogController($timeout, $scope, $stateParams, $uibModalInstance, entity, VotingAnswer, VotingContent) {
        var vm = this;

        vm.votingAnswer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.votingcontents = VotingContent.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.votingAnswer.id !== null) {
                VotingAnswer.update(vm.votingAnswer, onSaveSuccess, onSaveError);
            } else {
                VotingAnswer.save(vm.votingAnswer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('lvoteApp:votingAnswerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


    }
})();
