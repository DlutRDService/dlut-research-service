import torch
from transformers import AutoModelForCausalLM, AutoTokenizer

device = "cuda:0" # the device to load the model onto

model = AutoModelForCausalLM.from_pretrained(
    "mistralai/Mistral-7B-Instruct-v0.1",
    torch_dtype=torch.bfloat16
)
model.to(device)

user_input = input(": ")
# tokenizer = AutoTokenizer.from_pretrained("mistralai/Mistral-7B-Instruct-v0.1")
#
# messages = [
#     {"role": "user", "content": "Do you have mayonnaise recipes?"}
# ]
#
# encodeds = tokenizer.apply_chat_template(messages, return_tensors="pt")
#
# model_inputs = encodeds.to(device)

#
# generated_ids = model.generate(model_inputs, max_new_tokens=1000, do_sample=True)
#
# decoded = tokenizer.batch_decode(generated_ids)
# print(decoded[0])
