from threading import Thread

import torch
from transformers import AutoModelForCausalLM, AutoTokenizer, TextIteratorStreamer

device = "cuda:0" # the device to load the model onto

model = AutoModelForCausalLM.from_pretrained(
    "mistralai/Mistral-7B-Instruct-v0.1",
    torch_dtype=torch.bfloat16
)
model.to(device)

# user_input = input(": ")
tokenizer = AutoTokenizer.from_pretrained("mistralai/Mistral-7B-Instruct-v0.1")
user_input = ('Summarize this English abstract to about 150-200 words.\"This paper shows that masked autoencoders ('
              'MAE) are scalable self-supervised learners for computer vision. Our MAE approach is simple: we mask '
              'random patches of the input image and reconstruct the missing pixels. It is based on two core designs. '
              'First, we develop an asymmetric encoder-decoder architecture, with an encoder that operates only on the visible subset of patches (without mask tokens), along with a lightweight decoder that reconstructs the original image from the latent representation and mask tokens. Second, we find that masking a high proportion of the input image, for example, 75%, yields a nontrivial and meaningful self-supervisory task. Coupling these two designs enables us to train large models efficiently and effectively: we accelerate training (by 3x or more) and improve accuracy. Our scalable approach allows for learning high-capacity models that generalize well: for example, a vanilla ViT-Huge model achieves the best accuracy (87.8%) among methods that use only ImageNet-IK data. Transfer performance in downstream tasks outperforms supervised pretraining and shows promising scaling behavior.\"'
              )
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