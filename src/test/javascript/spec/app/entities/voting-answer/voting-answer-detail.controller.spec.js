'use strict';

describe('Controller Tests', function () {

    describe('VotingAnswer Management Detail Controller', function () {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVotingAnswer, MockVotingContent;
        var createController;

        beforeEach(inject(function ($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVotingAnswer = jasmine.createSpy('MockVotingAnswer');
            MockVotingContent = jasmine.createSpy('MockVotingContent');


            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'VotingAnswer': MockVotingAnswer,
                'VotingContent': MockVotingContent
            };
            createController = function () {
                $injector.get('$controller')("VotingAnswerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function () {
            it('Unregisters root scope listener upon scope destruction', function () {
                var eventType = 'lvoteApp:votingAnswerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
