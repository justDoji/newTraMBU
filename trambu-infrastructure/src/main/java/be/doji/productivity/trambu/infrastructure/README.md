# Infrastructure Layer

## Domain Properties

| | |
|---|---
|Name|  Infrastructure -- Cross domain layer
|Importance| Plumbing
|Type| Technical

## Context

This layer is used to house the plumbing of the application.
Is is brought to life to separate the technical concerns from the 
problem domain. 

## Design
The data will be fetched and persisted into plain text files,
just as in the original application.
Internally, a in-memory database will be used for quick and easy access to entities.


# Thesaurius

| Concept | Definition 
|---|---
| Parser | Gets data from a source and converts it to a data object
| Repository | The entity that stores domain objects and keeps track of them.
| TO | **T**ransfer **O**bject, raw data entity that contains only data. Does not follow the definition of *"Object"* in the OO-paradigm. It is more of a C-style **struct**.

