from threading import Thread

import torch
from transformers import AutoModelForCausalLM, AutoTokenizer, TextStreamer, TextIteratorStreamer

device = "cuda:0" # the device to load the model onto

model = AutoModelForCausalLM.from_pretrained(
    "mistralai/Mistral-7B-Instruct-v0.1",
    torch_dtype=torch.bfloat16
)
model.to(device)

# user_input = input(": ")
tokenizer = AutoTokenizer.from_pretrained("mistralai/Mistral-7B-Instruct-v0.1")
user_input = 'Can u speak Chinese'

messages = [
    {"role": "user", "content": user_input}
]

streamer = TextIteratorStreamer(tokenizer, skip_prompt=True, skip_special_tokens=True)

encodeds = tokenizer.apply_chat_template(messages, return_tensors="pt")

model_inputs = encodeds.to(device)

# Run the generation in a separate thread, so that we can fetch the generated text in a non-blocking way.
generation_kwargs = dict(input_ids=model_inputs, max_new_tokens=512, streamer=streamer, do_sample=True)

thread = Thread(target=model.generate, kwargs=generation_kwargs)
thread.start()
for item in streamer:
    print(item)