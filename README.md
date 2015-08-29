# FindBlock
Gives x/y/z coordinates for a specific block type in a region selected via WorldEdit

- Uses the WorldEdit API (*Aka, requires the WorldEdit plugin!*)
- ~~Some blocks aren't currently found and may throw errors. This is a known issue and is being looked into.~~ All blocks are currently supported via deprecated ```getId()```. It will be left this way until the transition to block names is complete.
