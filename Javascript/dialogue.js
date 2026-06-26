class Dialogue {
    constructor(steps, onEnd = null) {
        this.steps = steps;
        this.onEnd = onEnd;
    }

    static create() {
        return new DialogueBuilder();
    }

    async play(renderer) {
        return await new DialogueRuntime(renderer).execute(this);
    }
}

class DialogueBuilder {
    constructor() {
        this.steps = [];
        this.onEnd = null;
    }

    say(text) { // TODO
        this.steps.push({
            type: "say",
            text
        });

        return this;
    }

    pause(ms) { // TODO
        this.steps.push({
            type: "pause",
            duration: ms
        });

        return this;
    }

    choice(prompt) {
        return new ChoiceBuilder(this, prompt);
    }

    endDialogue() {
        this.steps.push({type: "end"});

        return this;
    }

    onFinish(callback) {
        this.onEnd = callback;

        return this;
    }

    addStep(step) {
        this.steps.push(step);
    }

    build() {
        return new Dialogue(this.steps, this.onEnd);
    }
}

class ChoiceBuilder {
    constructor(parent, prompt) {
        this.parent = parent;
        this.prompt = prompt;
        this.options = [];
    }

    option(label, condition = null, branch = null) {
        if (typeof condition === "function" && branch === null && condition.length > 0) {
            branch = condition;
            condition = null;
        }

        let dialogue = null;

        if (branch) {
            const builder = new DialogueBuilder();

            branch(builder);

            dialogue = builder.build();
        }

        this.options.push({
            label,
            condition,
            branch: dialogue
        });

        return this;
    }

    end() {
        this.parent.addStep({
            type: "choice",
            prompt: this.prompt,
            options: this.options
        });

        return this.parent;
    }
}

class DialogueResult {
    constructor() {
        this.completed = false;
        this.choices = {};
    }

    choice(prompt) {
        return this.choices[prompt];
    }

    recordChoice(prompt, option) {
        this.choices[prompt] = option;
    }
}

class DialogueRuntime {
    constructor(renderer) {
        this.renderer = renderer;
    }

    async execute(dialogue) {
        const result = new DialogueResult();

        await this.runSteps(dialogue.steps, result);

        result.completed = true;
        if (dialogue.onEnd) dialogue.onEnd();

        return result;
    }

    async runSteps(steps, result) {
        for (const step of steps) {
            const run = await this.runStep(step, result);

            if (!run) return;
        }
    }

    async runStep(step, result) {
        switch (step.type) {
            case "say":
                await this.renderer.renderText(step.text);
                return true;

            case "pause":
                await this.renderer.wait(step.duration);
                return true;

            case "end":
                return false;

            case "choice":
                await this.runChoice(step, result);
                return true;
        }

        return true; // skip unknown types
    }

    async runChoice(choice, result) {
        const available = choice.options.filter(o => !o.condition || o.condition());
        if (!available.length) return;

        await this.renderer.renderChoice(choice.prompt, available.map(o => o.label));

        const index = await this.renderer.getChoiceInput(available.length);
        const selected = available[index];

        result.recordChoice(choice.prompt, selected.label);

        if (selected.branch) await this.runSteps(selected.branch.steps, result);
    }
}