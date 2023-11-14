FROM node:16.20.0

WORKDIR /app

COPY . .

RUN npm ci

# Build du frontend
RUN npm run build

EXPOSE 3000

CMD ["npm", "start"]
