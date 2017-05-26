(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingContentDeleteController',VotingContentDeleteController);

    VotingContentDeleteController.$inject = ['$uibModalInstance', 'entity', 'VotingContent'];

    function VotingContentDeleteController($uibModalInstance, entity, VotingContent) {
        var vm = this;

        vm.votingContent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VotingContent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
