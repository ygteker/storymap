INSERT INTO UserJourney (id, title) VALUES (999, 'Checkout Flow');
INSERT INTO Release (id, name) VALUES (999, 'v1.0');
INSERT INTO UserStep (id, user_journey_id, title) VALUES (999, 999, 'Enter Address');
INSERT INTO IssueAssignment (gitlabIssueId, id, user_step_id, release_id) VALUES (123, 999, 999, 999);
