from dao import Dataset


data = Dataset('../data/conference2023', "TI_name", )


dataset = [{
      "instruction": "Introduce the paper '{TI_name}'.".format(TI_name=i.TI_name),
      "input": "",
      "output": "This paper 'title' was published in {PY} in the journal {SO}, authored by {AF} and others. "
                "It falls within the {WC} field, focusing mainly on {DE}. "
                "The research background of this paper is {research background}. "
                "This paper {research method}, and ultimately yielded {research results}, {research conclusion}."
} for i in data]