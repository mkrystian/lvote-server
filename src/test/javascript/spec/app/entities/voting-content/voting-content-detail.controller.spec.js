'use strict';

describe('Controller Tests', function() {

    describe('VotingContent Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVotingContent, MockVotingAnswer, MockVoting;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVotingContent = jasmine.createSpy('MockVotingContent');
            MockVotingAnswer = jasmine.createSpy('MockVotingAnswer');
            MockVoting = jasmine.createSpy('MockVoting');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'VotingContent': MockVotingContent,
                'VotingAnswer': MockVotingAnswer,
                'Voting': MockVoting
            };
            createController = function() {
                $injector.get('$controller')("VotingContentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'lvoteApp:votingContentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
