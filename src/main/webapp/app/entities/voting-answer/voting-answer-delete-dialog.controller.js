(function () {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingAnswerDeleteController', VotingAnswerDeleteController);

    VotingAnswerDeleteController.$inject = ['$uibModalInstance', 'entity', 'VotingAnswer'];

    function VotingAnswerDeleteController($uibModalInstance, entity, VotingAnswer) {
        var vm = this;

        vm.votingAnswer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete(id) {
            VotingAnswer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
