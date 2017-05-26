(function() {
    'use strict';

    angular
        .module('lvoteApp')
        .controller('VotingDeleteController',VotingDeleteController);

    VotingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Voting'];

    function VotingDeleteController($uibModalInstance, entity, Voting) {
        var vm = this;

        vm.voting = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Voting.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
