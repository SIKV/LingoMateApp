---
name: create-pull-request
description: This skill should be used when the user asks to create a pull request.
---

# Create Pull Request

Create well-documented pull requests with comprehensive descriptions.

## Behavior
1. Analyze commits since branching from main.
2. Generate a descriptive PR title no longer than 85 symbols. 
3. Add one or more of the following prefixes to the PR title:
    - [Shared] - for a PR that contains changes outside the /composeApp and /iosApp modules.
    - [Android] - for a PR that contains changes in the /composeApp module.
    - [iOS] - for a PR that contains changes in the /iosApp module.
4. Create a description with:
    - Short summary.
    - List of changes.
5. Create PR via `gh pr create`

## PR Template
```markdown
## Summary
Brief description of changes. No longer than 2-3 sentences.

## Changes
- List of specific changes made.
```

## Requirements
- GitHub CLI (`gh`) installed and authenticated.
- On a feature branch (not main).
