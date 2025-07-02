INSERT INTO UserJourney (id, title) VALUES (1, 'Checkout Flow');
INSERT INTO Release (id, name) VALUES (1, 'v1.0');
INSERT INTO UserStep (id, user_journey_id, title) VALUES (1, 1, 'Enter Address');
INSERT INTO IssueAssignment (gitlabIssueId, id, user_step_id, release_id) VALUES (123, 1, 1, 1);
